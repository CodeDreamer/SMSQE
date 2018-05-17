; Function to return name of HOTKEY  V2.00    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_name$
        xdef    hot_gnam

        xref    hot_thus
        xref    hot_thfr
        xref    hot_fitem
        xref    hk_getpr
        xref    ut_gxnm1
        xref    ut_rtsst

        include 'dev8_ee_hk_data'

;+++ 
; Return name of Hotkey
;
; string$ = HOT_NAME$ (key or name)
;---
hot_name$
        jsr     ut_gxnm1                 ; get one string
        bne.s   hn_exit
        jsr     hot_thus                 ; find hotkey
        bne.s   hn_exit
        jsr     hot_fitem                ; find item
        bne.s   hn_noname
        jsr     hot_gnam                 ; find name
        bra.s   hn_rtsst
hn_noname
        moveq   #0,d1
hn_rtsst
        jsr     ut_rtsst                 ; return sub string

hn_exfr
        jmp     hot_thfr                 ; free hotkey
hn_exit
        rts
        page
;+++
; gets pointer (a4) and length d1 of 'name' of hotkey
;
;       a1      called:   pointer to item
;       a4      returned: pointer to name
;       d1      returned: length of name
;       status return 0
;---
hot_gnam
        cmp.w   #hki.stbf,hki_type(a1)   ; stuff buffer?
        beq.s   hn_stbf                  ; ... yes, find it
        cmp.w   #hki.stpr,hki_type(a1)   ; stuff previous?
        beq.s   hn_stpr                  ; ... yes

        lea     hki_name(a1),a4
hn_retnm
        move.w  (a4)+,d1                 ; name length
        bra.s   hn_retz                  ; return it

hn_stpr
        move.l  hki_ptr(a1),-(sp)        ; save pointer
        jsr     hk_getpr                 ; get previous
        move.l  hki_ptr(a1),a4
        move.l  (sp)+,hki_ptr(a1)        ; and reset pointer
        bra.s   hn_pstbf
hn_stbf
        move.l  hki_ptr(a1),a4           ; point to stuff string
hn_pstbf
        move.l  a4,d1
hn_fend
        tst.b   (a4)+                    ; end?
        bne.s   hn_fend

        subq.l  #1,a4                    ; end
        exg     a4,d1
        sub.l   a4,d1                    ; length
hn_retz 
        moveq   #0,d0                    ; set status
        rts
        end
