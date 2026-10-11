$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
$lines = Get-Content dp.txt
for ($i=0; $i -lt $lines.Count; $i++) {
  if ($lines[$i] -match 'BlockState m_7417_') {
    Write-Host ('LINE ' + $i + ': ' + $lines[$i])
  }
}
