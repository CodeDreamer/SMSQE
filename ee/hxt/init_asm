; HOTKEY Extensions Initialisation  V2.00       1990  Tony Tebby   QJUMP

        xdef    hxt_init
        xdef    hxt_name

        xref    hxt_thing
        xref    hxt_procs
        xref    gu_thini
        xref    gu_tpadd

        xref.l  hxt_vers

        include 'dev8_keys_thg'

        section version
        dc.w    'Hotkey Extensions  V'
        dc.l    hxt_vers
        dc.b    ' ',$a

        section exten

hxt_defn dc.l    th_name+hxt.name+2
         dc.l    hxt_thing-*
         dc.l    hxt_vers
hxt.name equ     14
hxt_name dc.w    hxt.name,'Hotkey Control'

;+++
; Initialise the HOTKEY Extensions Thing and add extensions procedures.
;
;       status return standard
;---
hxt_init
hxti.reg reg    a1
        move.l  a1,-(sp)

        lea     hxt_defn,a1               ; extensions thing, version and name
        jsr     gu_thini
        bne.s   hxt_exit

        lea     hxt_procs,a1
        jsr     gu_tpadd

hxt_exit
        move.l  (sp)+,a1
        rts
        end
