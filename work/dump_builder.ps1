$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
jar xf ..\..\tools\forge-1.20.1-47.2.0-srg.jar 'net/minecraft/world/level/block/entity/BlockEntityType$Builder.class' 2>$null
javap -p 'net\minecraft\world\level\block\entity\BlockEntityType$Builder.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\betb.txt
Write-Host "done"
