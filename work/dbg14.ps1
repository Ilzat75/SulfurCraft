$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '--- Block: use/remove methods ---'
javap -cp $cp net.minecraft.world.level.block.Block 2>&1 | Select-String -Pattern 'InteractionResult|onRemove|use|MenuProvider|m_6262|m_7787|m_7246' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- Containers dropContents ---'
javap -cp $cp net.minecraft.world.level.block.Containers 2>&1 | Select-String -Pattern 'dropContents|Container' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- BlockEntityType methods (create) ---'
javap -cp $cp net.minecraft.world.level.block.entity.BlockEntityType 2>&1 | Select-String -Pattern 'create|m_58952|m_58919|BlockPos' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- AbstractContainerMenu stillValid helper usage: ContainerLevelAccess.get ---'
