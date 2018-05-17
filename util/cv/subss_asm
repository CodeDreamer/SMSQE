; Substitute Substring in string    V2.00    1990  Tony Tebby   QJUMP

        section cv

        xdef    cv_subss

        include 'dev8_keys_err'

;+++
; Substitutes a string for a substring in a string. If the resulting string is
; too long, it returns an error.
;
;       Registers:
;               Entry                           Exit
;       d0      pointer to old substring        error code
;       d1      length of substring             preserved
;       d2      max length of string            preserved
;       a1      pointer to string               preserved
;       a2      pointer to substitute string    preserved
;       all other registers preserved
;       status standard (err.inam if new length too great)
;---
cv_subss
css.reg reg    d1/d3/d4/d5/d6/a1/a2
        movem.l css.reg,-(sp)
        move.w  (a1),d6                  ; old name length
        move.w  (a2)+,d5                 ; length of new bit
        move.w  d5,d3
        add.w   d5,d6
        sub.w   d1,d6                    ; total length
        add.w   d0,d5                    ; end of new bit
        add.w   d0,d1                    ; end of old bit
        move.w  d0,d4                    ; start of old/new bit

        cmp.w   d2,d6                    ; name too long?
        bgt.s   css_inam                 ; ... no

css_do
        move.w  d6,(a1)+                 ; ... string length
        sub.w   d5,d1                    ; string increased or decreased?
        bge.s   css_down
css_up
        add.w   d6,a1                    ; start at top end
        sub.w   d5,d6                    ; copying this much
        lea     (a1,d1.w),a0
        bra.s   css_uele
css_uelp
        move.b  -(a0),-(a1)
css_uele
        dbra    d6,css_uelp

        add.w   d3,a2                    ; end of new bit
        bra.s   css_usle
css_uslp
        move.b  -(a2),-(a1)
css_usle
        dbra    d3,css_uslp
        bra.s   css_exok

css_down
        add.w   d4,a1                    ; put new bit in here
        bra.s   css_dsle
css_dslp
        move.b  (a2)+,(a1)+
css_dsle
        dbra    d3,css_dslp

        tst.w   d1                       ; any distance to move?
        beq.s   css_exok                 ; ... no
        lea     (a1,d1.w),a0
        sub.w   d5,d6                    ; amount to move
        bra.s   css_dele
css_delp
        move.b  (a0)+,(a1)+
css_dele
        dbra    d6,css_delp

css_exok
        moveq   #0,d0
css_exit
        movem.l (sp)+,css.reg
        rts
css_inam
        moveq   #err.inam,d0
        bra.s   css_exit
        end
