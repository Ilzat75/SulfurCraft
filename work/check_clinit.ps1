$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
Write-Host '=== static init (clinit) - f_83037 / f_83038 / f_83036 ==='
$lines = Get-Content shapes_bc.txt
$start = ($lines | Select-String -Pattern 'static \{\}' | Select-Object -First 1).LineNumber
if ($start) {
  $lines[($start-1)..($start+40)] | Select-String -Pattern 'f_83037_|f_83038_|f_83036_|Box|AABB|Shapes|putstatic' | ForEach-Object { $_.Line }
}
