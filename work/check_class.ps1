$ErrorActionPreference = 'Stop'
$dir = "C:\Users\User\Desktop\SulfurCraft\work\jarcheck"
Set-Location $dir
$src = Join-Path (Join-Path $dir 'sulfurcraft') 'SulfurCraft$SunflowerCropBlock.class'
$dst = Join-Path $dir 'SunflowerCropBlock.class'
Copy-Item -LiteralPath $src -Destination $dst -Force
javap -p -c $dst | Out-File -Encoding utf8 methods.txt
Get-Content methods.txt | Select-String -Pattern 'm_5940_|SPROUT_SHAPE|TALL_SHAPE|m_7417_|VoxelShape|class ' | ForEach-Object { $_.Line }
