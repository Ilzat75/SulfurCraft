$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
$jar = "..\..\tools\forge-1.20.1-47.2.0-srg.jar"
$classes = @(
  'net/minecraft/world/level/block/Block',
  'net/minecraft/world/level/block/BaseEntityBlock',
  'net/minecraftforge/common/extensions/IForgeBlock',
  'net/minecraftforge/common/extensions/IForgeMenuType',
  'net/minecraft/network/FriendlyByteBuf',
  'net/minecraft/client/resources/sounds/SimpleSoundInstance',
  'net/minecraft/client/gui/screens/inventory/AbstractContainerScreen',
  'net/minecraft/world/inventory/DataSlot',
  'net/minecraft/world/inventory/ContainerData',
  'net/minecraftforge/common/ForgeHooks',
  'net/minecraft/client/resources/sounds/SoundInstance',
  'net/minecraft/sounds/SoundSource',
  'net/minecraftforge/client/event/RegisterMenuScreensEvent',
  'net/minecraft/world/inventory/ContainerMenu'
)
foreach ($c in $classes) { jar xf $jar "$c.class" 2>$null }

function Show($cls, $patterns, $title) {
  Write-Host "===== $title ====="
  $lines = javap -p $cls 2>$null
  foreach ($p in $patterns) {
    $m = $lines | Select-String -Pattern $p
    if ($m) { $m | ForEach-Object { Write-Host ($_.Line.Trim()) } }
  }
}

Show 'net\minecraft\world\level\block\Block.class' @('getTicker|m_155232_|newBlockEntity|m_155231_|m_155233_') 'Block'
Show 'net\minecraft\world\level\block\BaseEntityBlock.class' @('newBlockEntity|getTicker|m_155232_|m_182500_') 'BaseEntityBlock'
Show 'net\minecraftforge\common\extensions\IForgeBlock.class' @('newBlockEntity|getTicker') 'IForgeBlock'
Show 'net\minecraftforge\common\extensions\IForgeMenuType.class' @('create|static') 'IForgeMenuType'
Show 'net\minecraft\network\FriendlyByteBuf.class' @('readBlockPos|m_130141_') 'FriendlyByteBuf'
Show 'net\minecraft\client\resources\sounds\SimpleSoundInstance.class' @('SimpleSoundInstance\(|m_119869_|forRecord|forBlockEntity|m_119875_|m_119876_|m_119877_') 'SimpleSoundInstance'
Show 'net\minecraft\client\gui\screens\inventory\AbstractContainerScreen.class' @('AbstractContainerScreen\(|m_7856_|m_8831_|m_7286_|render|m_181709_') 'AbstractContainerScreen'
Show 'net\minecraft\world\inventory\DataSlot.class' @('m_38893_|forContainer|DataSlot\(|m_38896_|m_38889_') 'DataSlot'
Show 'net\minecraft\world\inventory\ContainerData.class' @('m_6411_|m_6410_|m_6412_') 'ContainerData'
Show 'net\minecraftforge\common\ForgeHooks.class' @('getBurnTime|m_249073_') 'ForgeHooks'
Show 'net\minecraft\client\resources\sounds\SoundInstance.class' @('m_79075_') 'SoundInstance'
Show 'net\minecraft\sounds\SoundSource.class' @('f_11915_|f_11916_|f_11917_') 'SoundSource'
Show 'net\minecraftforge\client\event\RegisterMenuScreensEvent.class' @('register') 'RegisterMenuScreensEvent'
