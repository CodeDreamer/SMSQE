; Set error message in d0    1989  Tony Tebby

        section gen_util

        xdef    gu_serms

;+++
; Called to set a user defined error message
;
;       d0 r    error code
;       a1 c  p pointer to error message
;       status returned negative (standard)
;---
gu_serms
        move.l  a1,d0                    ; point to message
        bset    #31,d0                   ; set MSBit
        tst.l   d0                       ; and condition codes
        rts
        end
