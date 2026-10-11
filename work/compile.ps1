$ErrorActionPreference = 'Stop'
$root = "C:\Users\User\Desktop\SulfurCraft"
$src = Join-Path $root "sulfurcraft\mdk\src\main\java"
$cp = Join-Path $root "tools\forge-1.20.1-47.2.0-srg.jar"
$lib = Join-Path $root "tools\lib\*"
$out = Join-Path $root "work\classes"
New-Item -ItemType Directory -Force -Path $out | Out-Null

$files = Get-ChildItem -Path $src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$sourcesTxt = Join-Path $root "work\sources.txt"
[System.IO.File]::WriteAllLines($sourcesTxt, $files, (New-Object System.Text.UTF8Encoding $false))

Write-Host ("compiling " + $files.Count + " files")
$errLog = Join-Path $root "work\compile_errors.txt"
$javacArgs = "--release 17 -encoding UTF8 -cp `"$cp;$lib`" -d `"$out`" `"@$sourcesTxt`""
cmd /c "javac $javacArgs 2> `"$errLog`""
if (Test-Path $errLog) { Get-Content $errLog | ForEach-Object { Write-Host $_ } }
if ($LASTEXITCODE -eq 0) { Write-Host "COMPILE OK" } else { Write-Host ("COMPILE FAILED exit=" + $LASTEXITCODE) }
