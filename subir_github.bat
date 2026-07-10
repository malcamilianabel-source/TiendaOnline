@echo off
chcp 65001 > nul
echo ============================================
echo   SUBIR TiendaOnline a GitHub
echo   https://github.com/malcamilianabel-source/TiendaOnline
echo ============================================
echo.

cd /d "%~dp0"

echo [1/5] Inicializando repositorio git...
git init
git branch -M main

echo [2/5] Configurando usuario...
git config user.name "Abel Malca"
git config user.email "malcamilianabel@gmail.com"

echo [3/5] Agregando archivos...
git add .
git status --short

echo [4/5] Haciendo commit...
git commit -m "Proyecto TiendaOnline - Diseño de Patrones UTP"

echo [5/5] Conectando y subiendo a GitHub...
git remote remove origin 2>nul
git remote add origin https://github.com/malcamilianabel-source/TiendaOnline.git
git push -u origin main

if %ERRORLEVEL%==0 (
    echo.
    echo ============================================
    echo   LISTO! Revisa:
    echo   github.com/malcamilianabel-source/TiendaOnline
    echo ============================================
) else (
    echo.
    echo [INFO] Si pidio credenciales y fallo, abre Git Bash
    echo        en esta carpeta y ejecuta:
    echo        git push -u origin main
)

echo.
pause
