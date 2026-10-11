$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
jar xf ..\..\tools\forge-1.20.1-47.2.0-srg.jar 'net/minecraft/tags/BlockTags.class' 2>$null
javap -p 'net\minecraft\tags\BlockTags.class' | Out-File -Encoding utf8 btags.txt
Write-Host '=== BlockTags fields (dirt/dirt_like) ==='
Get-Content btags.txt | Select-String -Pattern 'f_130399_|DIRT|dirt' | ForEach-Object { $_.Line }
Write-Host '=== all static tag fields (first 20) ==='
Get-Content btags.txt | Select-String -Pattern 'public static final net.minecraft.tags.TagKey' | Select-Object -First 20 | ForEach-Object { $_.Line }
