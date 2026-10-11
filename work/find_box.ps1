$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
jar xf ..\..\tools\forge-1.20.1-47.2.0-srg.jar 'net/minecraft/world/level/block/Block.class' 'net/minecraft/world/phys/shapes/Shapes.class' 2>$null
javap -p 'net\minecraft\world\level\block\Block.class' | Out-File -Encoding utf8 block_cls.txt
javap -p 'net\minecraft\world\phys\shapes\Shapes.class' | Out-File -Encoding utf8 shapes_cls.txt
Write-Host '=== Block.box (static, 6 doubles) ==='
Get-Content block_cls.txt | Select-String -Pattern 'static.*VoxelShape.*double.*double.*double.*double.*double.*double' | ForEach-Object { $_.Line }
Write-Host '=== Shapes.or / Shapes.empty ==='
Get-Content shapes_cls.txt | Select-String -Pattern 'm_83110_|m_83111_|or\(|empty\(' | ForEach-Object { $_.Line }
