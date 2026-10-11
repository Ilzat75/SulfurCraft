$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
$lines = Get-Content dp.txt
$idx = -1
for ($i=0; $i -lt $lines.Count; $i++) {
  if ($lines[$i] -match 'public net\.minecraft\.world\.level\.block\.state\.BlockState m_7417_') { $idx = $i; break }
}
Write-Host "found at line $idx"
$lines[$idx..($idx+140)] | Out-File -Encoding utf8 tail2.txt
Get-Content tail2.txt
