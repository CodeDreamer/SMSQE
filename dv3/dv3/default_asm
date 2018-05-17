; DV3 Default routines          V3.00                 1992 Tony Tebby

        section dv3

        xdef    dv3_inv
        xdef    dv3_ok
        xdef    dv3_done
        xdef    dv3_logphys
        xdef    dv3_rts

        include 'dev8_dv3_keys'
        include 'dev8_keys_err'

dv3_inv
        moveq   #err.nimp,d0
        rts
dv3_ok
        moveq   #0,d0
        rts
dv3_done
        move.b  #ddf.unlock,ddf_lock(a4)
        rts
dv3_logphys
        mulu    ddf_asect(a4),d0
        add.l   d1,d0
        add.l   ddf_lsoff(a4),d0
dv3_rts
        rts
        end
