; DV3 make directory            V3.00           1992 Tony Tebby

        section dv3

        xdef    dv3_mkdr

        xref    dv3_ckro

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'
;+++
; DV3 make directory
;---
dv3_mkdr
        jsr     dv3_ckro                 ; read only?
        move.l  ddf_fhlen(a4),d0
        sub.l   d3c_feof(a0),d0          ; empty file?
        bne.s   dmd_iu                   ; ... no
        jmp     ddf_drmake(a4)           ; ... yes, make directory
dmd_iu
        moveq   #err.fdiu,d0
        rts
        end
