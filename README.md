# ArashiPolice-1.1.0 
## Download  
[ArashiPolice-1.1.0.jar](https://github.com/Smile-NS/ArashiPolice/raw/master/target/ArashiPolice-1.1.0.jar)  
## What is this?  
* 荒らしを拘束するためのSpigotプラグイン  
* JAILワールドを設定することで投獄することができます 
* GUIで簡単に操作ができます(コマンドでも可能です)  
* UUIDとIPの両方で荒らしを判別します  
#### 制限可能な行動一覧  
<dl>
  <dt>MOVE</dt>
  <dd>プレイヤーの移動を禁止します</dd>
  <dt>CHAT</dt>
  <dd>プレイヤーのチャットメッセージ送信を禁止します</dd>
  <dt>ATTACK</dt>
  <dd>プレイヤーの攻撃を禁止します</dd>
  <dt>BREAK</dt>
  <dd>プレイヤーのブロック破壊を禁止します</dd>
  <dt>PLACE</dt>
  <dd>プレイヤーのブロック設置を禁止します</dd>
  <dt>INTERACT</dt>
  <dd>プレイヤーの相互作用する行動(ボタンやドアの開閉など)を禁止します</dd>
</dl>　　

## How to use?  
#### 基本操作  
1. `/police` でプレイヤーリストを開きます  
2. プレイヤーのアイコンを**左クリック**でペナルティオプションを開けます
3. プレイヤーのアイコンを**右クリック**で投獄オプションを開けます
#### JAILワールドの設定方法  
* `/police jail world [ワールド名]`で設定可能です  
* ここで設定したワールドに投獄されるようになります   
#### 荒らしの判別方法について  
* geyserやリレーサーバー等を使用している場合、プレイヤーのIPが重複することがあります  
* そういった場合は`/police exempt-ip register [IP]`で重複するIPを免除してください  
* こうすることでUUIDのみでの判別になります  
#### config.ymlについて
* 荒らしの行動を禁止すると荒らし側に「○○をすることは禁止されています」のようなメッセージが  
 表示されるようになっています  
* [咲暇鯖](https://minecraft.jp/servers/saki269.ddns.net?__cf_chl_jschl_tk__=20790ab2022926856f921de77b038446318d7481-1617342096-0-ASGGBINpJcJSLSSwogMAL_dhkqQ8KkBEdloHDjnE6Q43fGOKe6gIPF6RwMLNRiUFGkFaSsWsiDVYDu1zqnWpQDeLxFfzVpIBfcvPuM_pLq05CkFOat6gnVGz_gJKx2KfRLdnXaglsBb-k2N3lPf0xO7RA0kHjdi-qe8ZUYL9yGB56-lz6-ELDu44Rg02MRy9HXB_6_z0sYzJxoSYr6OZcWG7t8_MehfETYl4TisPxGBqgJRH9JW16aJkwOI6SIv-0nQSUO7OY5XuNlKPuBiWwtg8vj9q5NTZgA_PVrxXY1lpWcrY5n0Nd6vxC7JQNfQQWmNO0LwwILhWv3r2W1QwjIz7E_1cHRoxcQ2l0XKMu520T6a-fZwOOoOwlLm2EEUebeywb1JTp0z5OHIeszGXHxMaanubQ0YlRMvYXwV-L0BX#/stats) という治安の悪いサーバーで使用することを想定して作ったので「ドンマイ！(*ﾟ∀ﾟﾉﾉ」のような  
 荒らしへの煽りメッセージが同時に表示されるようになっています  
* このメッセージを無効にしたい際は`plugins/ArashiPolice/config.yml`の中の`do-not-mind`というタグの値を`""`に  
 書き換えてください(他の煽りメッセージにしたい場合もここを書き換えることで設定可能です)  
* 荒らしの行動制限を行ったり、投獄した際に全体チャットでログが表示されるようになっていますが、非表示にしたい場合は  
  同様にconfig.yml内の`broadcast`というタグの値をfalseに書き換えてください
  
