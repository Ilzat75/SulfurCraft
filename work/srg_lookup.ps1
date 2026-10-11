$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"

function Show($cls, $patterns, $title) {
  Write-Host "===== $title ====="
  $lines = javap -p $cls 2>$null
  foreach ($p in $patterns) {
    $m = $lines | Select-String -Pattern $p
    if ($m) { Write-Host "--- $p ---"; $m | ForEach-Object { Write-Host ($_.Line.Trim()) } }
  }
}

Show 'net\minecraft\world\inventory\AbstractContainerMenu.class' @('m_38889_|m_51107_|m_182426_|m_150376_|Slot\(|m_39287_|m_39631_|m_150377_|m_150378_|m_150379_|m_8887_|m_8885_|m_39630_') 'AbstractContainerMenu'
Show 'net\minecraft\world\inventory\Slot.class' @('m_40218_|m_40213_|m_40216_|m_40217_|Slot\(|m_5609_|m_6654_|m_5868_') 'Slot'
Show 'net\minecraft\world\inventory\ContainerLevelAccess.class' @('m_46819_|m_121924_|m_39001_') 'ContainerLevelAccess'
Show 'net\minecraft\world\level\block\entity\BlockEntity.class' @('m_183515_|m_142466_|m_58904_|m_219203_|m_58900_|m_6596_|m_189515_|m_183516_') 'BlockEntity'
Show 'net\minecraft\world\level\block\entity\BlockEntityType.class' @('m_213929_|m_155496_|of\(|m_213930_') 'BlockEntityType'
Show 'net\minecraft\world\Container.class' @('m_6893_|m_6897_|m_6836_|m_6894_|m_6828_|m_6892_|m_6891_|m_6890_|m_39292_') 'Container'
Show 'net\minecraft\world\entity\player\Inventory.class' @('m_6893_|m_35975_|m_36001_|m_36004_|m_35974_|m_36000_|m_36003_') 'Inventory'
Show 'net\minecraft\sounds\SoundEvent.class' @('m_11582_|m_11583_|m_11584_') 'SoundEvent'
Show 'net\minecraft\world\item\ItemStack.class' @('m_41619_|m_41777_|m_41720_|m_41751_|m_41611_|m_41783_|m_41784_|m_41613_|m_204121_|m_41794_|m_41795_') 'ItemStack'
