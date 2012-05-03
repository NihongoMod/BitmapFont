BitmapFont
==========

Minecraftでビットマップフォント（画像を使ったフォント）を利用できるようにするMODです。<br>
デフォルトよりきれいなフォントの描画が可能になります。

なお、最近追加されたBidiには対応していません。
あくまで英語と日本語のみの利用を前提としています。

## 免責事項
当MODを使用して発生した、いかなる障害・損害について、<br>
それが例え当MODが原因であったとしても開発者は一切責任を負いません。

## ダウンロード
Bitmap Font v1.1 (for 1.2.5) (2012.05.04)

## 動作環境
Windows7 Ultimate 64bit/JavaSE 7 64bitでのみ動作確認しています。<br>
それ以外の環境では動作しない場合があります。<br>
あらかじめご了承ください。

## インストール方法
当MODは動作に **ModLoader** を必要とします。<br>
あらかじめインストールしておいてください。<br>
※ Minecraftの本体が展開されているフォルダを MCROOT/ とします。<br>
　 Windowsならば "%APPDATA%/.minecraft" に展開されています。

ダウンロードしたzipファイルを展開し、<br>
中のファイルをすべて MCROOT/ にコピーもしくは上書きしてください。

【MCPatcherを利用する場合】
    MCPatcher を起動し、MCROOT/mods/[バージョン]/BitmapFont.zip を追加してください。
    なお、BitmapFontの位置をModLoaderより下にする必要があります。
    また、HD Fontとは併用できないのでチェックを外してください。

【MCPatcherを利用しない場合】
    MCROOT/mods/[バージョン]/BitmapFont.zip を MCROOT/mods/BitmapFont.zip にコピーしてください。
    これでModLoaderが自動的に読み込むようになります。

## 設定ファイル
MCROOT/config/BitmapFont.properties ファイルをテキストエディタで編集して設定を変更できます。

    baseLine
        フォントの上下の表示位置を調整するパラメータです。
        値が大きくすると上にズレます。

    enableMod
        フォントの置き換えを行うかどうかを設定するパラメータです。
        0 -> off
        1 -> on

    fontFile
        使用するフォントファイルのパスを設定するパラメータです。
        MCROOT/resources/fontフォルダ以下のパスを指定してください。

    scale
        文字の表示スケールを調整するパラメータです。
        フォントによっては値を大きくすると綺麗に見えるかもしれません。
        デフォルトでは 1.25 に設定されていますが、他のフォントを使う場合は変更してください。

## Q&A

<pre>
Q. フォントを差し替えたいんだけど？
A. フォントを変更するには Bitmap Font Generator ( http://www.angelcode.com/products/bmfont/ )を
   利用すると便利です。生成したフォントファイル(\*\*\*.fntと\*\*\*.png) を resources/font/ 内に配置したあと、
   設定ファイルを編集してください。
</pre>

<pre>
Q. フォントを差し替えたけど表示がデフォルトのままなんだけど？
A. フォントを読み込めなかった場合、デフォルトのまま起動します。
   設定ファイルを見なおしてみてください。
</pre>

## 更新履歴
<pre>
v1.1
  ・バージョン1.2.5に対応
</pre>

<pre>
v1.0
  ・バージョン1.2.3に対応
  ・NihongoMODから分離
</pre>

## 謝辞
デフォルトのフォントに VLゴシック(http://vlgothic.dicey.org/) を使わせて頂きました。
ありがとうございます。