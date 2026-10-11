$root = "C:\Users\User\Desktop\SulfurCraft"
$lib = Join-Path $root "tools\lib"
$md = "C:\Users\User\.gradle\caches\forge_gradle\maven_downloader"
$want = @('datafixerupper','netty-buffer','netty-common','netty-transport','netty-codec','netty-resolver','authlib','gson','guava','brigadier')
$copied = @()
Get-ChildItem $md -Recurse -Filter *.jar -ErrorAction SilentlyContinue | ForEach-Object {
   $n = $_.Name.ToLower()
   foreach ($w in $want) {
      if ($n -like ($w.ToLower() + "-*.jar") -or $n -eq ($w.ToLower() + ".jar")) {
         $dest = Join-Path $lib $_.Name
         if (-not (Test-Path $dest)) { Copy-Item $_.FullName $dest; $copied += $_.Name }
         break
      }
   }
}
Write-Host ("copied: " + ($copied -join ', '))
