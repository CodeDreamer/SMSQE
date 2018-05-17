; Thing Initialisation         V1.00    1990    Tony Tebby  QJUMP

        section gen_util

        xdef    gu_thini

        xref    gu_achpp
        xref    gu_thzlk

        include 'dev8_keys_thg'
        include 'dev8_mac_assert'

;+++
; Create a Thing. The linkage block is filled with sensible defaults.
;
; The pointer to the extension Thing definition should point to 3 long words
; and the Thing name:
;                       long word linkage block alloc
;                       long relative pointer to Thing (or 0)
;                       long word Version ID
;                       Thing name
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A1      Pointer to Extension def        base of linkage block
;---
gu_thini
gti.reg reg     a0/a2
        movem.l gti.reg,-(sp)
        move.l  (a1)+,d0
        jsr     gu_achpp                 ; allocate linkage
        bne.s   gti_exit
        move.l  a1,a2                    ; thing
        move.l  (a2)+,d0
        beq.s   gti_sver
        add.l   d0,a1
        move.l  a1,th_thing(a0)          ; pointer to thing
gti_sver
        assert  th_verid,th_name-4
        lea     th_verid(a0),a1
        move.l  (a2)+,(a1)+              ; version
        move.w  (a2)+,d0
        move.w  d0,(a1)+
        bra.s   gti_cend
gti_cloop
        move.b  (a2)+,(a1)+              ; name
gti_cend
        dbra    d0,gti_cloop

        move.l  a0,a1                    ; we need this as well for return

        jsr     gu_thzlk                 ; link in

gti_exit
        movem.l (sp)+,gti.reg
        rts
        end
