$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '--- BlockBehaviour: InteractionResult + remove ---'
javap -cp $cp net.minecraft.world.level.block.state.BlockBehaviour 2>&1 | Select-String -Pattern 'InteractionResult|onRemove|m_7787|m_6262' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- find Containers class ---'
& jar tf $cp | Select-String -Pattern '/Containers\.class$' | ForEach-Object { Write-Host $_.Line }
Write-Host '--- net.minecraft.world.Containers dropContents ---'
javap -cp $cp net.minecraft.world.Containers 2>&1 | Select-String -Pattern 'dropContents' | ForEach-Object { Write-Host $_.Line.Trim() }
