; DV3 Medium Change Utility        V3.00           1992 Tony Tebby

        section dv3

        xdef    dv3_change

        xref    dv3_sbclr

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'
        include 'dev8_mac_assert'

;+++
; DV3 Medium Change
;
;       a4 c  p drive definition
;
;       status return standard
;---
dv3_change
        tst.b   ddf_lock(a4)             ; locked?
        beq.s   dch_change               ; ... no, changed medium
        move.b  #ddf.mchg,ddf_mstat(a4)  ; ... bad status
        moveq   #err.mchk,d0
        rts

dch_change
        assert  ddf.mnew,0
        sf      ddf_mstat(a4)            ; new medium
        jmp     dv3_sbclr                ; and clear slave blocks
        end
