; Routine to get HOTKEY key  V2.01    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_gtky

        xref    ut_gtnm1

        include 'dev8_keys_err'

;+++
; Get hotkey key into d7, and update name table pointer
;
;       d1-d3   scratch
;       d7  r   (word) Hotkey
;       a1/a2   scratch
;       a3 cr   name table pointer
;---
hot_gtky
        jsr     ut_gtnm1                 ; first string
        bne.s   hgk_rts
        addq.l  #8,a3                    ; next one
        cmp.l   a3,a5
        ble.s   hgk_ipar                 ; ... there is not one

        cmp.w   #1,(a6,a1.l)             ; one character only?
        bne.s   hgk_ipar
        moveq   #0,d7
        move.b  2(a6,a1.l),d7            ; keep character
        tst.l   d0
        rts

hgk_ipar
        moveq   #err.ipar,d0
hgk_rts
        rts
        end
