* CALL command   V1.0    1985    Tony Tebby   QJUMP
*
        section exten
*
*       CALL address, parameters
*
        xdef    call
        xdef    call_fix
*
        xref    ut_gtlin
*
        include dev8_sbsext_ext_keys
*
call
        bsr.l   ut_gtlin                get some long integers
        bne.s   call_rts                ... oops
        ext.l   d3
        lsl.w   #2,d3                   convert count to bytes
        beq.s   call_bp                 ... oops
        add.l   d3,bv_rip(a6)           and remove parameters from RI stack
        move.l  (a6,a1.l),-(sp)         put first one as return address!
        movem.l 4(a6,a1.l),d1-d7/a0-a5  set parameters
call_bp
        moveq   #err.bp,d0              preset err.bp
call_rts
        rts

call_fix
        addq.l  #4,sp
        rts
        end
