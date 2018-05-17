* Execute code from a Thing             v0.00   Apr 1988  J.R.Oakley  QJUMP
*                                       V0.01              T Tebby 
        section thing
*
        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
*
        xref    gu_exec
        xref    th_entry
        xref    th_enfix
*
        xdef    th_exec
        xdef    th_exfix

th_exfix
        moveq   #th_enfix-th_entry,d0    ; use fixed entry point
        bra.s   thx_do
*+++
* This uses the general execute utility gu_EXEC to execute code provided by
* an executable Thing.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      owner, or 0, or -1              job ID
*       D2      priority | timeout              preserved
*       A0      file name                       preserved
*       A1      parameter string                preserved
*       stack   FEXEC $0c+gu_EXEC
*---
th_exec
        moveq   #0,d0
thx_do
thxreg  reg     a2/a4
        movem.l thxreg,-(sp)
        lea     th_entry(pc),a4
        add.w   d0,a4
        lea     thx_ucod(pc),a2         ; point to user code
        jsr     gu_exec(pc)             ; and use it to execute a file
        movem.l (sp)+,thxreg
        rts
*
thx_ucod
        bra.s   thx_read
        nop
        bra.s   thx_open
        nop
thx_clos
txcreg  reg     d0/a0/a1
        movem.l txcreg,-(sp)
        move.l  a2,a0                   ; we're only interested in the name
        moveq   #sms.fthg,d0            ; free the Thing
        jsr     (a4)
        movem.l (sp)+,txcreg
        tst.l   d0
        rts
*
* This copies the Job header, and also gets an instance of the Thing
* for the Job that's going to execute its code.
*
thx_read
txrreg  reg     d3/a0-a3
        movem.l txrreg,-(sp)
        move.l  thh_hdrs(a0),a3         ; offset of start of header
        add.l   a0,a3                   ; thus absolute address
        move.l  thh_hdrl(a0),d0         ; size of header
        move.l  d0,d3                   ; keep it
        bra.s   ex_cpthe
ex_cpthl
        move.b  (a3)+,(a1)+             ; copy header
ex_cpthe
        subq.l  #1,d0
        bpl.s   ex_cpthl
*
        move.l  a2,a0                   ; this Thing
        moveq   #0,d3                   ; now or never
        moveq   #sms.uthg,d0            ; is to be used by the new Job
        jsr     (a4)
txr_exit
        movem.l (sp)+,txrreg
        rts
*
thx_open
txoreg  reg     a2
        movem.l txoreg,-(sp)
        move.l  a0,a2                   ; save name in case of error
        moveq   #0,d3
        moveq   #sms.uthg,d0            ; try to use thing
        jsr     (a4)
        bne.s   txo_exit                ; oops
        move.l  a1,a0                   ; get to correct register
*
        moveq   #err.ipar,d0            ; assume the worst
        cmp.l   #thh.flag,thh_flag(a0)  ; standard Thing?
        bne.s   txo_excl                ; no
        move.l  thh_type(a0),d2         ; get type
        subq.l  #1,d2                   ; is it executable?
        bne.s   txo_excl                ; ... no
*
        move.l  thh_hdrl(a0),d2         ; length of file
        move.l  thh_data(a0),d3         ; data space required
        move.l  thh_strt(a0),a1         ; and start address
        add.l   a0,a1
        moveq   #0,d0
txo_exit
        movem.l (sp)+,txoreg
        rts
txo_excl
        bsr     thx_clos
        bra.s   txo_exit
*
        end
