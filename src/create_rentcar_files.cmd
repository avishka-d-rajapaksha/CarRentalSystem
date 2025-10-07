@echo off
setlocal EnableExtensions EnableDelayedExpansion

rem Determine project root
set "ROOT=%~dp0"
if exist "%ROOT%src" (
  set "PROJECT_DIR=%ROOT%"
) else if exist "%ROOT%rentCar\src" (
  set "PROJECT_DIR=%ROOT%rentCar"
) else (
  echo [ERROR] Could not find 'src' folder in "%ROOT%" or "%ROOT%rentCar".
  echo Place this script in the project root (rentCarKATEX_INLINE_CLOSE and run again.
  exit /b 1
)

set "SRC_DIR=%PROJECT_DIR%\src"
set "OUT_DIR=%PROJECT_DIR%\out"
set "BUILD_DIR=%OUT_DIR%\classes"
set "SOURCES_FILE=%OUT_DIR%\sources.txt"

echo.
echo === rentCar build script ===
echo Project: %PROJECT_DIR%
echo.

rem Ensure output dirs
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%" >nul
if not exist "%SRC_DIR%\com\mycompany\rentcar\dao" mkdir "%SRC_DIR%\com\mycompany\rentcar\dao" >nul
if not exist "%SRC_DIR%\com\mycompany\rentcar\util" mkdir "%SRC_DIR%\com\mycompany\rentcar\util" >nul

rem 1) Fix InvoiceItem typo: file rename and in-file references
echo [Step 1/4] Fixing InvoiceItem typo (if any)...
if exist "%SRC_DIR%\com\mycompany\rentcar\Invoiceltem.java" (
  ren "%SRC_DIR%\com\mycompany\rentcar\Invoiceltem.java" "InvoiceItem.java"
  echo - Renamed Invoiceltem.java -> InvoiceItem.java
)

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$src = Get-Item -LiteralPath '%SRC_DIR%';" ^
  "Get-ChildItem -Path $src -Recurse -Filter *.java | ForEach-Object {" ^
  "  $c = Get-Content -LiteralPath $_.FullName -Raw;" ^
  "  $new = $c -replace '\bInvoiceltem\b','InvoiceItem';" ^
  "  if ($new -ne $c) { Set-Content -LiteralPath $_.FullName -Value $new -Encoding utf8 }" ^
  "}"

rem 2) Create DAO classes and TestDAOs.java
echo [Step 2/4] Creating DAO classes and TestDAOs.java...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$ErrorActionPreference='Stop';" ^
  "$src = [IO.Path]::GetFullPath('%SRC_DIR%');" ^
  "$daoDir = Join-Path $src 'com\mycompany\rentcar\dao';" ^
  "$utilDir = Join-Path $src 'com\mycompany\rentcar\util';" ^
  "New-Item -ItemType Directory -Force -Path $daoDir | Out-Null;" ^
  "New-Item -ItemType Directory -Force -Path $utilDir | Out-Null;" ^
  "$entities = @('User','Vehicle','Booking','Payment','Invoice','InvoiceItem','Customer','VehicleMaintenance');" ^
  "foreach ($e in $entities) {" ^
  "  $cls = ""$e"" + 'DAO';" ^
  "  $code = @"" ^
package com.mycompany.rentcar.dao;

import com.mycompany.rentcar.$e;
import com.mycompany.rentcar.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class $cls {

    public Optional<$e> findById(int id) {
        String sql = ""SELECT * FROM $e WHERE id = ?"";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // TODO: map ResultSet -> $e
                    return Optional.empty();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public List<$e> findAll() {
        List<$e> list = new ArrayList<>();
        String sql = ""SELECT * FROM $e"";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // TODO: map rs -> $e and add to list
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean insert($e item) {
        // TODO: implement insert
        return false;
    }

    public boolean update($e item) {
        // TODO: implement update
        return false;
    }

    public boolean deleteById(int id) {
        String sql = ""DELETE FROM $e WHERE id = ?"";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
"@; ^
  "  $file = Join-Path $daoDir (""$cls.java"");" ^
  "  $code | Out-File -FilePath $file -Encoding utf8 -Force;" ^
  "}" ^
  "$test = @"" ^
package com.mycompany.rentcar.util;

import com.mycompany.rentcar.dao.*;

public class TestDAOs {
    public static void main(String[] args) {
        System.out.println(""=== TestDAOs ==="");

        // Try DB connection
        try (java.sql.Connection conn = DBConnection.getConnection()) {
            System.out.println(""DB connection: "" + ((conn != null && !conn.isClosed()) ? ""OK"" : ""NOT OK""));
        } catch (Throwable t) {
            System.out.println(""DB connection failed: "" + t.getClass().getSimpleName() + "": "" + t.getMessage());
        }

        Object[] daos = new Object[] {
            new UserDAO(), new VehicleDAO(), new BookingDAO(), new PaymentDAO(),
            new InvoiceDAO(), new InvoiceItemDAO(), new CustomerDAO(), new VehicleMaintenanceDAO()
        };

        for (Object dao : daos) {
            try {
                var m = dao.getClass().getMethod(""findAll"");
                Object result = m.invoke(dao);
                int size = (result instanceof java.util.Collection<?>)
                        ? ((java.util.Collection<?>) result).size() : -1;
                System.out.println(dao.getClass().getSimpleName() + "".findAll() -> "" + size);
            } catch (Throwable t) {
                System.out.println(dao.getClass().getSimpleName() + "".findAll() failed: "" + t.getClass().getSimpleName() + "": "" + t.getMessage());
            }
        }
    }
}
"@; ^
  "$testFile = Join-Path $utilDir 'TestDAOs.java';" ^
  "$test | Out-File -FilePath $testFile -Encoding utf8 -Force;"

rem 3) Build sources list
echo [Step 3/4] Collecting sources...
if exist "%SOURCES_FILE%" del /q "%SOURCES_FILE%"
for /R "%SRC_DIR%" %%F in (*.java) do (
  echo "%%F">>"%SOURCES_FILE%"
)

rem 4) Compile
echo [Step 4/4] Compiling with JDK 17...
set "JAVAC=javac"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC=%JAVA_HOME%\bin\javac.exe"

"%JAVAC%" -version

"%JAVAC%" -encoding UTF-8 -source 17 -target 17 -Xlint:none -d "%BUILD_DIR%" "@%SOURCES_FILE%"
if errorlevel 1 (
  echo.
  echo [ERROR] Compilation failed.
  exit /b 1
) else (
  echo.
  echo Build success. Classes -> %BUILD_DIR%
)

rem Copy resources onto classpath (optional)
if exist "%PROJECT_DIR%\src\main\resources" (
  xcopy /E /I /Y "%PROJECT_DIR%\src\main\resources\*" "%BUILD_DIR%\">nul
)

rem Optional: run quick test if asked
if /I "%~1"=="test" (
  set "JAVA=java"
  if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA=%JAVA_HOME%\bin\java.exe"
  echo.
  echo Running com.mycompany.rentcar.util.TestDAOs ...
  "%JAVA%" -cp "%BUILD_DIR%" com.mycompany.rentcar.util.TestDAOs
)

endlocal