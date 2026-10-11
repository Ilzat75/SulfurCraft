$root = "C:\Users\User\Desktop\SulfurCraft"
$res = Join-Path $root "sulfurcraft\mdk\src\main\resources\assets\sulfurcraft"
$texDir = Get-ChildItem $root -Directory | Where-Object { Test-Path (Join-Path $_.FullName "sunflower_oil.png") } | Select-Object -First 1 -ExpandProperty FullName
$sndDir = Get-ChildItem $root -Directory | Where-Object { Test-Path (Join-Path $_.FullName "oil_press_loop.ogg") } | Select-Object -First 1 -ExpandProperty FullName
Write-Host "texDir=$texDir"
Write-Host "sndDir=$sndDir"
Copy-Item (Join-Path $texDir "sunflower_oil.png") (Join-Path $res "textures\item\sunflower_oil.png") -Force
Copy-Item (Join-Path $texDir "french_fries.png") (Join-Path $res "textures\item\french_fries.png") -Force
New-Item -ItemType Directory -Force -Path (Join-Path $res "sounds") | Out-Null
Copy-Item (Join-Path $sndDir "oil_press_loop.ogg") (Join-Path $res "sounds\oil_press_loop.ogg") -Force
Copy-Item (Join-Path $sndDir "deep_fryer_loop.ogg") (Join-Path $res "sounds\deep_fryer_loop.ogg") -Force
Write-Host "copied items+sounds"
