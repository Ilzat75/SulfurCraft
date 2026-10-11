$ErrorActionPreference = 'Stop'
$root = "C:\Users\User\Desktop\SulfurCraft"
$stage = Join-Path $root "work\stage"
$classes = Join-Path $root "work\classes"
$res = Join-Path $root "sulfurcraft\mdk\src\main\resources"

# clean stage
if (Test-Path $stage) { Remove-Item $stage -Recurse -Force }
New-Item -ItemType Directory -Force -Path $stage | Out-Null

# copy resources
Copy-Item (Join-Path $res "*") $stage -Recurse -Force

# copy compiled classes (sulfurcraft + net if present)
Copy-Item (Join-Path $classes "sulfurcraft") $stage -Recurse -Force
$netDir = Join-Path $classes "net"
if (Test-Path $netDir) { Copy-Item $netDir $stage -Recurse -Force }

Write-Host "staged. top-level:"
Get-ChildItem $stage | Select-Object -ExpandProperty Name
