$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
javap -p 'net\minecraft\world\inventory\AbstractContainerMenu.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\acm.txt
javap -p 'net\minecraft\world\level\block\entity\BlockEntity.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\be.txt
javap -p 'net\minecraft\world\level\block\entity\BlockEntityType.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\bet.txt
javap -p 'net\minecraft\world\Container.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\cont.txt
javap -p 'net\minecraft\world\entity\player\Inventory.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\inv.txt
javap -p 'net\minecraft\sounds\SoundEvent.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\se.txt
javap -p 'net\minecraft\world\inventory\Slot.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\slot.txt
javap -p 'net\minecraft\world\inventory\ContainerLevelAccess.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\cla.txt
javap -p 'net\minecraft\world\inventory\MenuType.class' | Out-File -Encoding utf8 C:\Users\User\Desktop\SulfurCraft\work\mt.txt
Write-Host "dumped"
