$ErrorActionPreference = 'Stop'
$cp = "C:\Users\User\Desktop\SulfurCraft\tools\forge-1.20.1-47.2.0-srg.jar"
Write-Host '===== IForgeBlock (ticker/newBlockEntity) ====='
javap -cp $cp net.minecraftforge.common.extensions.IForgeBlock 2>&1 | Select-String -Pattern 'Ticker|BlockEntity|newBlockEntity|getTicker' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== IForgeMenuType ====='
javap -cp $cp net.minecraftforge.common.extensions.IForgeMenuType 2>&1 | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== FriendlyByteBuf readBlockPos ====='
javap -cp $cp net.minecraft.network.FriendlyByteBuf 2>&1 | Select-String -Pattern 'readBlockPos|m_130141_|writeBlockPos' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== DataSlot ====='
javap -cp $cp net.minecraft.world.inventory.DataSlot 2>&1 | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== ContainerData ====='
javap -cp $cp net.minecraft.world.inventory.ContainerData 2>&1 | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== ForgeHooks getBurnTime ====='
javap -cp $cp net.minecraftforge.common.ForgeHooks 2>&1 | Select-String -Pattern 'getBurnTime' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== SimpleSoundInstance (ctors + for methods) ====='
javap -cp $cp net.minecraft.client.resources.sounds.SimpleSoundInstance 2>&1 | Select-String -Pattern 'SimpleSoundInstance\(|m_11986|m_11987|forRecord|forBlockEntity|forUI' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== SoundSource constants ====='
javap -cp $cp net.minecraft.sounds.SoundSource 2>&1 | Select-String -Pattern 'f_1191' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== AbstractContainerScreen ctor/render ====='
javap -cp $cp net.minecraft.client.gui.screens.inventory.AbstractContainerScreen 2>&1 | Select-String -Pattern 'AbstractContainerScreen\(|m_7856_|render|m_181709_|m_280|blit|m_9770_' | ForEach-Object { Write-Host $_.Line.Trim() }
Write-Host '===== RegisterMenuScreensEvent ====='
javap -cp $cp net.minecraftforge.client.event.RegisterMenuScreensEvent 2>&1 | ForEach-Object { Write-Host $_.Line.Trim() }
