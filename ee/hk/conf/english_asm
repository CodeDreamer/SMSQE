; HOTKEY configuration   1988   T.Tebby   QJUMP

        section language

        xdef    hkc_english

        xref    hk_english

        xref.l  hk_vers

        xref    hk_stufc
        xref    hk_stufp
        xref    hk_stufl

        include 'dev8_mac_config'
        include 'dev8_mac_text'

hkc_english
        dc.b   $a,$a

        mkcfhead  {HOTKEY System II},hk_vers

        mkcfitem string,'D',mxl_dnam,,,\
          {Default drive for executable programs: if you have Toolkit II,\}\
          {the HOTKEY system will use the program default}

        mkcfitem char,'K',hk_stufc,,,\
          {Keyboard Stuffer HOTKEY character. This stuffs the current\}\
          {string in the HOTKEY stuffer buffer into the current keyboard\}\
          {queue. Strings are put into the stuffer buffer by QRAM and other\}\
          {utility programs or by the QPTR command HOT_STUFF},%10111

        mkcfitem char,'P',hk_stufp,,,\
          {Previous string Keyboard Stuffer HOTKEY character. This is\}\
          {similar to the normal Keyboard Stuffer but it accesses the\}\
          {string defined before the current stuffer string},%10111

        mkcfitem long,'S',hk_stufl,,,\
          {Stuffer buffer size: this limits the number of stuffer strings\}\
          {that may be held at one time},$80,$4000
        mkcfend

        mkcfstr dnam,12,{flp1_}; default directory

        end
