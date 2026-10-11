$ErrorActionPreference = 'Stop'
Set-Location "C:\Users\User\Desktop\SulfurCraft\work\decomp"
Write-Host '=== Shapes static VoxelShape fields ==='
Get-Content shapes_cls.txt | Select-String -Pattern 'static.*VoxelShape' | ForEach-Object { $_.Line }
