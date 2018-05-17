; HOTKEY configuration   1988   T.Tebby   QJUMP

        section language

        xdef    hkc_french

        xref    hk_french

        xref.l  hk_vers

        xref    hk_stufc
        xref    hk_stufp
        xref    hk_stufl

        include 'dev8_mac_config'
        include 'dev8_mac_text'

hkc_french
        dc.b   $a,$a

        mkcfhead  {Systême HOTKEY II},hk_vers

        mkcfitem string,'U',mxl_dnam,,,\
          {UnitÉ par dÉfaut pour les programmes exÉcutables: Si vous avez\}\
          {le Toolkit II, le systême HOTKEY utilisera le dÉfaut programme}

        mkcfitem char,'H',hk_stufc,,,\
          {Touche pour le buffer HOTKEY. Un appui sur cette touche insêre\}\
          {le contenu de ce buffer dans le tampon clavier courant. Des\}\
          {chaïnes sont insÉrÉes dans ce buffer par QRAM et d'autres\}\
          {utilitaires, ou par la commande HOT_STUFF de QPTR},%10111

        mkcfitem char,'P',hk_stufp,,,\
          {Touche pour le buffer HOTKEY prÉcÉdent. Ceci est similaire\}\
          {ç la touche normale pour le buffer HOTKEY, mais insêre la\}\
          {chaïne prÉcÉdente du buffer HOTKEY dans le tampon clavier\}\
          {plutòt que la chaïne courante contenue dans ce buffer},%10111

        mkcfitem long,'T',hk_stufl,,,\
          {Taille buffer HOTKEY: limite le nombre de chaïnes qui peuvent\}\
          {ëtre contenues dans ce buffer un moment donnÉ}

        mkcfend

        mkcfstr dnam,12,{flp1_}; default directory

        end
