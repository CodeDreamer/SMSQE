; HOTKEY configuration   1988   T.Tebby   QJUMP

        section language

        xdef    hkc_german
        xref    hk_german

        xref.l  hk_vers

        xref    hk_stufc
        xref    hk_stufp
        xref    hk_stufl

        include 'dev8_mac_config'
        include 'dev8_mac_text'

hkc_german
        dc.b   $a,$a

        mkcfhead  {HOTKEY System II},hk_vers

        mkcfitem string,'D',mxl_dnam,,,\
          {Voreingestelltes Ger€t f‡r ausf‡hrbare Programme; wenn Sie\}\
          {Toolkit II besitzen, nimmt das HOTKEY System die Programm-\}\
          {Voreinstellung}

        mkcfitem char,'K',hk_stufc,,,\
          {Taste zum Abrufen des HOTKEY-Puffers. Dies bringt den aktuellen\}\
          {String des HOTKEY-Puffers in den aktuellen Tastaturpuffer.\}\
          {Strings werden von QRAM und anderen Programmen, z.B. mit dem\}\
          {QPTR Befehl HOT_STUFF, in den HOTKEY-Puffer gebracht},%10111

        mkcfitem char,'P',hk_stufp,,,\
          {Taste zum Abrufen des Vorherigen String HOTKEY Puffers. Dies\}\
          {funktioniert €hnlich wie der normale HOTKEY-Puffer, ergibt\}\
          {jedoch den vor dem letzten String definierten String},%10111

        mkcfitem long,'S',hk_stufl,,,\
          {HOTKEY-Puffer-Gr„œe: dies begrenzt die Anzahl der Strings die\}\
          {gleichzeitig gespeichert werden k„nnen},$80,$4000
        mkcfend

        mkcfstr dnam,12,{win1_}; default directory

        end
