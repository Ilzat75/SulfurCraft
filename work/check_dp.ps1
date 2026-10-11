$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
javap -c -p 'net\minecraft\world\level\block\DoublePlantBlock.class' | Out-File -Encoding utf8 dp.txt
Write-Host '===== m_5573_ (getStateForPlacement) ====='
Get-Content dp.txt | Select-String -Pattern 'm_5573_' -Context 0,40 | ForEach-Object { $_.Context.PreContext + $_.Line + $_.Context.PostContext }
Write-Host '===== m_7898_ (canSurvive) ====='
Get-Content dp.txt | Select-String -Pattern 'm_7898_' -Context 0,30 | ForEach-Object { $_.Context.PreContext + $_.Line + $_.Context.PostContext }
