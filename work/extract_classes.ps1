$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
$jar = "..\..\tools\forge-1.20.1-47.2.0-srg.jar"
$classes = @(
  'net/minecraft/world/inventory/AbstractContainerMenu',
  'net/minecraft/world/inventory/Slot',
  'net/minecraft/world/inventory/ContainerLevelAccess',
  'net/minecraft/world/inventory/MenuType',
  'net/minecraft/world/level/block/entity/BlockEntity',
  'net/minecraft/world/level/block/entity/BlockEntityType',
  'net/minecraft/world/Container',
  'net/minecraft/world/entity/player/Inventory',
  'net/minecraft/sounds/SoundEvent',
  'net/minecraft/world/item/ItemStack',
  'net/minecraft/core/Direction'
)
foreach ($c in $classes) {
  jar xf $jar "$c.class" 2>$null
}
Write-Host "extracted"
