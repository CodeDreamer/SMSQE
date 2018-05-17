; HOTKEY Thing Extension Parameter Defs   V2.00     1990  Tony Tebby  QJUMP

        section exten

        xdef    hxt_prmk
        xdef    hxt_prmx
        xdef    hxt_prms
        xdef    hxt_prks

        include 'dev8_keys_thg'

hxt_prks
         dc.w  thp.char          ; Hotkey
         dc.w  thp.call+thp.str  ; compulsory string
         dc.w  0

hxt_prmk
         dc.w  thp.char          ; Hotkey
hxt_prmx
         dc.w  thp.call+thp.str  ; compulsory string

         dc.w  -'P'              ; parameter
         dc.w  thp.call+thp.str  ; ... string

         dc.w  -'J'              ; Job name
         dc.w  thp.call+thp.str  ; ... string

         dc.w  -'W'              ; Wake name
         dc.w  thp.call+thp.str  ; ... string

         dc.w  0

hxt_prms
         dc.w    thp.char        ; Hotkey
hxt_prrs
         dc.w    thp.rep
           dc.w    thp.call+thp.str ; repeated strings
         dc.w    thp.rep
         dc.w    0
         end
