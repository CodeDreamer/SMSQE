; Check line                          V2.00   1989  Tony Tebby

        section pars

        xdef    ps_cknul

        include 'dev8_keys_err'
        include 'dev8_keys_k'

;+++
; Check command line.
;
; This routine checks for as null command line.
;
; If the line starts with a comment line character, the line is nulled.
;
;       Registers:
;               Entry                           Exit
;       D0      comment line character           0 = null line
;                                                first character on line
;
;       A1      pointer to command line         first non space
;
;       Status as D0.b
;---
ps_cknul
        cmp.b   (a1),d0                  ; comment line
        bne.s   pcn_skst
        moveq   #0,d0                    ; ... yes
        sf      (a1)
        rts

pcn_skst
        moveq   #' ',d0
pcn_sksp
        cmp.b   (a1)+,d0                 ; skip to non-space
        beq.s   pcn_sksp
        move.b  -(a1),d0                 ; set next char
        rts
        end
