Add-Type -AssemblyName System.IO.Compression.FileSystem
$z = [System.IO.Compression.ZipFile]::OpenRead("C:\Users\User\Desktop\SulfurCraft\sulfurcraft-1.2.jar")
$names = $z.Entries | ForEach-Object { $_.FullName }
Write-Host "--- net/ ---"
$names | Where-Object { $_ -like "net/*" }
Write-Host "--- sounds/gui/new items ---"
$names | Where-Object { $_ -match "sounds/|gui/|sunflower_oil|french_fries|sounds.json" }
$z.Dispose()
# deploy
Copy-Item "C:\Users\User\Desktop\SulfurCraft\sulfurcraft-1.2.jar" "C:\Users\User\AppData\Roaming\.minecraft\mods\sulfurcraft-1.2.jar" -Force
Write-Host "deployed to mods"
