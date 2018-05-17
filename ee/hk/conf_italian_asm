; HOTKEY configuration   1988   T.Tebby   QJUMP

        section language

        xdef    hkc_italian

        xref    hk_italian

        xref.l  hk_vers

        xref    hk_stufc
        xref    hk_stufp
        xref    hk_stufl

        include 'dev8_mac_config'
        include 'dev8_mac_text'

hkc_italian
        dc.b   $a,$a

        mkcfhead  {Programma HOTKEY II},hk_vers

        mkcfitem string,'D',mxl_dnam,,,\
	{Unitç di default per l'esecuzione dei programmi : se hai il Toolkit II,\}\
	{il programma HOTKEY utilizzerç il programma di default}

        mkcfitem char,'K',hk_stufc,,,\
	{Carattere della tastiera per il buffer HOTKEY. Premendo questo carattere\}\
	{si inserisce la stringa di caratteri nel buffer della tastiera\}\
	{Le stinge di caratteri sono inserite nel buffer della tastiera da QRAM o altri\}\
	{programmi di utilitç o dal comando HOT_STUFF di QTPR},%10111

        mkcfitem char,'P',hk_stufp,,,\
	{Tasto per il buffer HOTKEY precedente. Questo ê\}\
	{simile al tasto normale per il buffer HOTKEY, ma inserisce\}\
	{la stringa definita precedentemente},%10111

        mkcfitem long,'S',hk_stufl,,,\
	{Dimensione buffer HOTKEY: limita il numero delle stringhe \}\
	{che possone essere contenute contemporaneamente},$80,$4000
        mkcfend

        mkcfstr dnam,12,{flp1_}; default directory

        end
