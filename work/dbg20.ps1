$root = "C:\Users\User\Desktop\SulfurCraft"
Write-Host '=== tools/lib contents ==='
Get-ChildItem (Join-Path $root "tools\lib") -Filter *.jar | ForEach-Object { Write-Host $_.Name }
Write-Host '=== find datafixerupper / netty in tools ==='
Get-ChildItem (Join-Path $root "tools") -Recurse -Filter *.jar | Where-Object { $_.Name -match 'datafixer|netty' } | ForEach-Object { Write-Host $_.FullName }
$cp = Join-Path $root "tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '=== BlockEntitySupplier ==='
& jar tf $cp | Select-String -Pattern 'BlockEntitySupplier' | ForEach-Object { Write-Host $_.Line }
javap -cp $cp 'net.minecraft.world.level.block.entity.BlockEntityType$BlockEntitySupplier' 2>&1 | ForEach-Object { Write-Host ([string]$_) }
Write-Host '=== TickableSoundInstance ==='
javap -cp $cp net.minecraft.client.resources.sounds.TickableSoundInstance 2>&1 | ForEach-Object { Write-Host ([string]$_) }
Write-Host '=== Block onRemove (search BlockBehaviour full for void m_ with BlockState,Level,BlockPos,BlockState,boolean) ==='
javap -cp $cp net.minecraft.world.level.block.state.BlockBehaviour 2>&1 | Select-String -Pattern 'boolean, net.minecraft.world.level.block.state.BlockState\)|m_7787|onRemove' | ForEach-Object { Write-Host $_.Line.Trim() }
