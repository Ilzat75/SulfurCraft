$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
javap -c -p 'net\minecraft\world\phys\shapes\Shapes.class' | Out-File -Encoding utf8 shapes_bc.txt
Write-Host '=== m_83040_ ==='
Get-Content shapes_bc.txt | Select-String -Pattern 'm_83040_\(\)' -Context 0,8 | ForEach-Object { $_.Line; $_.Context.PostContext } | Select-Object -First 9
Write-Host '=== m_83144_ ==='
Get-Content shapes_bc.txt | Select-String -Pattern 'm_83144_\(\)' -Context 0,8 | ForEach-Object { $_.Line; $_.Context.PostContext } | Select-Object -First 9
