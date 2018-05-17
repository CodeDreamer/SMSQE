; Functions to set load and execute HOTKEYs  V2.02    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_load
        xdef    hot_lod1

        xref    hot_park
        xref    hot_ldit
        xref    hot_thact
        xref    hk_newst

        xref    hot_rter
        xref    gu_achpp
        xref    gu_rchp

        include 'dev8_ee_hk_data'
        include 'dev8_ee_hk_xhdr'

;+++
; error = HOT_LOAD (key, filename |program name| |I| |G|P|U| |window|memory|)
;---
hot_load
        moveq   #hki.xfil,d6             ; execute file
        bra.s   hld_do
hot_lod1
        moveq   #hki.wkxf,d6             ; try wake first

hld_do
        swap    d6
        jsr     hot_park                 ; get parameters
        bne.s   hld_rts                  ; ... oops

        moveq   #hkh.plen+hki_name+6,d0  ; allocate heap item
        add.w   (a6,a1.l),d0             ; including variable length file name
        jsr     gu_achpp
        bne.s   hld_rter

        swap    d6
        jsr     hot_ldit                 ; set up load item

        move.l  a0,a1                    ; item
        move.w  d7,d1                    ; key
        lea     hk_newst,a2
        jsr     hot_thact                ; do it
        beq.s   hld_rter
hld_rchp
        jsr     gu_rchp                  ; can't do it
hld_rter
        jmp     hot_rter                 ; and return
hld_rts
        rts
        end
