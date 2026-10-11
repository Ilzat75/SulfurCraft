$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '--- Containers methods ---'
javap -cp $cp net.minecraft.world.Containers 2>&1 | ForEach-Object { Write-Host $_.Trim() }
Write-Host '--- Block.onRemove ---'
javap -cp $cp net.minecraft.world.level.block.Block 2>&1 | Select-String -Pattern 'm_7787|onRemove' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- BlockBehaviour.onRemove ---'
javap -cp $cp net.minecraft.world.level.block.state.BlockBehaviour 2>&1 | Select-String -Pattern 'm_7787|onRemove' | ForEach-Object { Write-Host $_.Line.Trim() }
