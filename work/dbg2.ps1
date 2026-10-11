$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
jar xf ..\..\tools\forge-1.20.1-47.2.0-srg.jar 'net/minecraftforge/common/extensions/IForgeBlock.class' 'net/minecraftforge/common/extensions/IForgeMenuType.class' 2>$null
Write-Host '===== IForgeBlock (ticker/newBlockEntity) ====='
javap -p 'net\minecraftforge\common\extensions\IForgeBlock.class' 2>$null | Select-String -Pattern 'Ticker|BlockEntity|newBlockEntity|getTicker' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== IForgeMenuType ====='
javap -p 'net\minecraftforge\common\extensions\IForgeMenuType.class' 2>$null | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== BaseEntityBlock ====='
javap -p 'net\minecraft\world\level\block\BaseEntityBlock.class' 2>$null | ForEach-Object { Write-Host $_.Line.Trim() }
