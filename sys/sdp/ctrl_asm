* sdp send control sequence        V2.00     1987  Tony Tebby  QJUMP
*
        section sdp
*
        xdef    sdp_ctrl
*
        xref    sdp_bytc
        xref    sdp_sbuf
*
        include dev8_sys_sdp_data
*
*       d0 cr   sequence key /  error code if return NZ
*       d1-d3   scratch
*       a1      scratch
*       a5 c  p pointer to data structure
*
sdp_ctrl
        move.l  dp_csequ(a5),a1          ; pointer to sequences
        add.w   d0,a1
        add.w   (a1),a1                  ; sequence required
dc_loop
        move.b  (a1)+,d1                 ; next byte
        beq.s   dc_exit                  ; end
        blt.s   dc_actn                  ; special action
dc_byte
        bsr.l   sdp_bytc                 ; send one byte
        beq.s   dc_loop                  ; ... ok
dc_exit
        rts
*
        dc.w    dc_null-dc_actn
        dc.w    dc_colr-dc_actn
        dc.w    dc_vdot-dc_actn
        dc.w    dc_vdot-dc_actn
        dc.w    dc_vdot-dc_actn
        dc.w    dc_hdot-dc_actn
        dc.w    dc_hdot-dc_actn
        dc.w    dc_hdot-dc_actn
        dc.w    dc_byte-dc_actn
dc_actn        
        move.b  d1,d0                    ; look up action address
        ext.w   d0
        move.b  d1,d2                    ; keep action
        move.b  (a1)+,d1                 ; next byte
        move.w  dc_actn(pc,d0.w),d0
        jmp     dc_actn(pc,d0.w)         ; jump to it
*
dc_colr
        subq.w  #1,a1                    ; no parameter!!
        move.w  dp_ptrpt(a5),d0          ; get pattern pointer
        move.b  -1(a4,d0.w),d1           ; colour key
        bra.s   dc_byte                  ; send byte
*
dc_null
        move.b  d1,d2                    ; number of nulls * 256
        lsl.w   #8,d2                    ; number of nulls
dc_nullp
        moveq   #0,d1                    ; null
        bsr.l   sdp_bytc                 ; send it
        bne.s   dc_exit
        subq.w  #1,d2
        bne.s   dc_nullp
        bra.s   dc_loop
*
dc_hdot
        moveq   #-dp.hdtbl,d0            ; ASCII LT, low/high Z, high/low GT
        add.b   d2,d0
        move.w  dp_hdots(a5),d2          ; set number of dots
        bra.s   dc_valu                  ; send value
*
dc_vdot
        moveq   #-dp.vdtbl,d0            ; ASCII LT, low/high Z, high/low GT
        add.b   d2,d0
        move.w  dp_vdots(a5),d2          ; set number of dots
dc_valu
        move.b  d1,d3                    ; keep number of bytes
        moveq   #0,d1
        move.b  (a1)+,d1                 ; get scaling
        add.w   d1,d2                    ; round up
        subq.w  #1,d2
        ext.l   d2
        divu    d1,d2                    ; scale it
        move.w  d2,d1                    ; where we want it
        tst.b   d0                       ; and convert for output
        blt.s   dc_ascii                 ; ASCII
        beq.s   dc_lohi                  ; low byte first
                         ; we need the data in order high byte first
        move.b  d3,d2                    ; first preposition the data
        subq.b  #1,d2
        lsl.w   #3,d2
        ror.l   d2,d1                    ; first byte is in the right place
        moveq   #24,d2                   ; rotate by 24 bits (-8 bits) each time
        bra.s   dc_bin
dc_lohi
        moveq   #8,d2                    ; rotate by 8 bits each time
dc_bin
        bsr.l   sdp_bytc
        bne.s   dc_exit
        ror.l   d2,d1                    ; and move next byte in
        subq.b  #1,d3
        bgt.s   dc_bin
        bra.l   dc_loop
*
dc_ascii
        bsr.l   sdp_sbuf                 ; clear buffer
        bne.l   dc_exit
        move.w  d3,dp_bufpt(a5)          ; update pointer
        move.w  #dp_buffr,d2             ; buffer offset 
        add.w   d3,d2                    ; end of filled biy
dc_asloop
        divu    #10,d1                   ; divide by 10
        swap    d1                       ; ... remainder
        add.b   #'0',d1                  ; in ASCII
        subq.w  #1,d2
        move.b  d1,(a5,d2.w)             ; into buffer
        clr.w   d1                       ; prepare result for next division
        swap    d1
        subq.w  #1,d3                    ; any more?
        bgt.s   dc_asloop                ; ... yes
        bra.l   dc_loop
        end
