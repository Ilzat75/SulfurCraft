$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
Write-Host '=== class files present? ==='
@('net\minecraft\world\level\block\Block.class','net\minecraft\world\level\block\BaseEntityBlock.class') | ForEach-Object { Write-Host ("$_ : " + (Test-Path $_)) }
Write-Host '=== find IForgeBlock / IForgeMenuType in jar ==='
jar tf ..\..\tools\forge-1.20.1-47.2.0-srg.jar | Select-String -Pattern 'IForgeBlock\.class|IForgeMenuType\.class|BaseEntityBlock\.class' | ForEach-Object { Write-Host $_.Line }
Write-Host '=== Block ticker methods ==='
javap -p 'net\minecraft\world\level\block\Block.class' 2>$null | Select-String -Pattern 'Ticker|BlockEntity|getTicker' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '=== BaseEntityBlock full ==='
javap -p 'net\minecraft\world\level\block\BaseEntityBlock.class' 2>$null | ForEach-Object { Write-Host $_.Line.Trim() }
