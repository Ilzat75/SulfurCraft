$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '--- ContainerHelper ---'
javap -cp $cp net.minecraft.world.item.ContainerHelper 2>&1 | ForEach-Object { Write-Host ([string]$_) }
Write-Host '--- Containers removeItem ---'
javap -cp $cp net.minecraft.world.level.block.Containers 2>&1 | Select-String -Pattern 'removeItem|m_18991|m_18994' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- Level sendBlockUpdated ---'
javap -cp $cp net.minecraft.world.level.Level 2>&1 | Select-String -Pattern 'sendBlockUpdated|m_46819' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '--- BlockPos relative(Direction) confirm ---'
javap -cp $cp net.minecraft.core.BlockPos 2>&1 | Select-String -Pattern 'm_121945' | ForEach-Object { Write-Host $_.Line.Trim() }
