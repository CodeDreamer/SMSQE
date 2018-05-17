* Retry sdump    V2.00       1987  Tony Tebby
*
        section sdp
*
        xdef    sdp_save        save screen dump status
        xdef    sdp_retry
*
        include dev8_keys_err
        include dev8_sys_sdp_data

sdp_save                 ; save status
        move.l  dp_pstk(a5),d0           ; pointer to stack

        movem.l dp.sreg,dp_save(a5)      ; save registers

        lea     dp_sstk(a5),a0           ; save stack
sdp_sstk
        move.l  (sp)+,(a0)+              ; ... until
        cmp.l   d0,sp                    ; top of stack?
        blt.s   sdp_sstk

        move.l  a0,dp_pstk(a5)           ; save top pointer
        moveq   #err.nc,d0
        rts

sdp_retry                ; retry using saved status
        lea     dp_sstk(a5),a0           ; bottom of save aera
        move.l  a0,d0
        move.l  dp_pstk(a5),a0           ; pointer to top of saved stack
        move.l  sp,dp_pstk(a5)

sdp_stack
        move.l  -(a0),-(sp)              ; stack
        cmp.l   d0,a0                    ; all of it
        bgt.s   sdp_stack

        lea     dp_save(a5),a0           ; restore registers
        movem.l (a0),dp.sreg
        rts
        end
