* Close SDUMP channel   V2.00     1985  Tony Tebby
*
        section sdp
*
        xdef    sdp_close
*
        include dev8_keys_qlv
        include dev8_keys_qdos_ioa
        include dev8_sys_sdp_ddlk
*
sdp_close
        move.w  mem.rchp,a2             return heap
        jmp     (a2)
        end
