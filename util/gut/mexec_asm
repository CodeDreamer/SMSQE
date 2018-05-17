* Execute code from memory     v0.00   May 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
*
        xref    gu_exec
*
        xdef    gu_mexec
*+++
* This uses the general execute utility GU_EXEC to execute code which is 
* resident in memory.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      owner, or 0, or -1              job ID
*       D2      priority | timeout              preserved
*       D3      dataspace                       preserved
*       A0      start of program                preserved
*       A1      parameter string                preserved
*       stack   MEXEC $08+GU_EXEC
*---
gu_mexec
ugmreg  reg     d4/a2
        movem.l ugmreg,-(sp)
        move.l  d3,d4                   ; pass dataspace required
        lea     ugm_ucod(pc),a2         ; point to user code
        jsr     gu_exec(pc)             ; and use it to execute
        movem.l (sp)+,ugmreg
        rts
*
ugm_ucod
        bra.s   ugm_read
        nop
        bra.s   ugm_open
        nop
ugm_clos
        moveq   #0,d0
        rts
*
* This copies the Job header
*
ugm_read
        move.l  d2,d0                   ; length to copy (always +ve even)
umr_loop       
        move.w  (a0)+,(a1)+             ; copy header
        subq.l  #2,d0 
        bgt.s   umr_loop

        sub.l   d2,a1                   ; restore code pointer
        rts
*
ugm_open
        move.l  d4,d3                   ; data space
        move.l  a0,a1                   ; start
        moveq   #8+2+1,d2               ; header length (zero length name)
        addq.l  #6,a0
        cmp.w   #$4afb,(a0)+            ; standard?
        bne.s   umo_exit                ; ... no
        add.w   (a0),d2
umo_exit
        bclr    #0,d2                   ; round up to even, already one too long
        move.l  a1,a0                   ; set pointer to header
        moveq   #0,d0
        rts
        end
