* General execute routine               v0.00   Apr 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_keys_jcb'
        include 'dev8_keys_qdos_sms'
*
        xdef    gu_exec
*+++
* This provides the core routine for starting up a job, wherever its
* code may be.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      owner, or 0, or -1              Job ID
*       D2      priority | timeout
*       A0      "name"
*       A1      parameter string
*       A2      read/open/close code
*       A3      as required by user code        preserved
*       A4-A6   as required by user code        updated only by user code
*
* The read/open/close code has three entry points for the three functions,
* at 0(A2), 4(A2) and 8(A2). They should act in the following manner:
*
* Read loads code into the job's code space.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      job ID                          preserved
*       D2      code length                     preserved
*       A0      "channel ID"                    preserved
*       A1      code space                      preserved
*       A2      "name" (for Thing)              preserved
*       A4-A6   unused by this routine          used as required
*
* Open returns the space required for the job, a "channel ID" for load
* and close, and an absolute start address if required:
* if it fails then the "file" should be "closed".
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D1      owner
*       D2                                      code length
*       D3                                      data space length
*       A0                                      "channel ID"
*       A1                                      start address or 0
*       A4-A6   unused by this routine          used as required
*
* Close closes the "channel", either after an error or after the code
* has been succesfully "loaded".
*
*       Registers:
*               Entry                           Exit
*       D0/CCR                                  preserved
*       D1      owner (for Things)              preserved
*       A0      "channel ID"                    preserved
*       A2      "name" (for Things)             preserved
*       A4-A6   unused by this routine          used as required
*---
gu_exec
uexreg  reg     d1-d3/a0-a3
*
        movem.l uexreg,-(sp)
        sub.w   #uex.fram,sp
stk_chid equ    $00
stk_jcln equ    $04
stk_jsiz equ    $08
stk_jbas equ    $0c
uex.fram equ    $10
stk_ownr equ    uex.fram+$00    ; owner is replaced by...
stk_jid  equ    uex.fram+$00    ; ...created Job ID
stk_prtm equ    uex.fram+$04
stk_name equ    uex.fram+$0c
stk_parm equ    uex.fram+$10
stk_ucod equ    uex.fram+$14
*
*
        moveq   #sms.myjb,d1            ; for myself
        jsr     4(a2)                   ; open the "file" and get the sizes
        bne     uex_exit                ; failed (file will be closed already)
        move.l  a0,stk_chid(sp)         ; keep channel ID
        move.l  d2,stk_jcln(sp)         ; and code length
        move.l  d2,d0
        add.l   d3,d0                   ; anticipated job size
        move.l  d0,stk_jsiz(sp)         ; keep it
*
* The file is open and we know how big it is, so we can create the Job.
*
        move.l  a1,a2                   ; keep start for now
        move.l  stk_parm(sp),a1         ; and restore parameter pointer
        move.l  a1,d0                   ; any parameters?
        beq.s   uex_crjb                ; no
        move.w  (a1),d0                 ; parameters are this long
        ext.l   d0
        addq.l  #1,d0
        bclr    #0,d0                   ; even them up
        add.l   d0,d3                   ; and add to data space
uex_crjb
        move.l  stk_ownr(sp),d1         ; restore the anticipated owner
        move.l  a2,a1                   ; and start address
        moveq   #sms.crjb,d0
        trap    #do.sms2                ; create 
        tst.l   d0                      ; OK?
        bne.s   uex_excl                ; no, exit closing file
        move.l  d1,stk_jid(sp)          ; keep Job ID
        moveq   #0,d2
        moveq   #sms.injb,d0            ; get round bug to find Job base
        trap    #do.sms2
        move.l  a0,stk_jbas(sp)         ; set base
*
* The Job is created, so we can copy the parameter block onto its stack
*
        move.l  stk_parm(sp),d0         ; are there any parameters?
        beq.s   uex_lcod                ; nope, so load the code
        move.l  a0,a1
        add.l   stk_jsiz(sp),a0         ; point to expected stack
        move.l  a0,jcb_a7-jcb_end(a1)   ; and fill it in in JCB
        move.l  d0,a1                   ; parameter block
        move.w  (a1)+,d0                ; its size is this
        bra.s   uex_cpre
uex_cprl
        move.b  (a1)+,(a0)+           
uex_cpre
        dbra    d0,uex_cprl
        move.l  stk_jbas(sp),a0         ; restore job base
*
* We can now load the code
*
uex_lcod
        move.l  a0,a1                   ; point to code space
        move.l  stk_chid(sp),a0         ; "channel" to load from
        move.l  stk_name(sp),a2         ; name of "file"
        move.l  stk_jid(sp),d1          ; owner of code
        move.l  stk_jcln(sp),d2         ; expected code length
        pea     uex_lret(pc)            ; where to return
        move.l  stk_ucod+4(sp),-(sp)    ; user code (read)
        rts                             ; call it
uex_lret
        bsr.s   uex_clos                ; close the file
        bne.s   uex_exrj                ; load failed, give up
*
* Everything's OK, so start the Job
*
        move.l  stk_jid(sp),d1
        movem.w stk_prtm(sp),d2/d3      ; required priority/timeout
        move.l  a3,-(sp)
        moveq   #sms.acjb,d0            ; activate it
        trap    #do.sms2
        move.l  (sp)+,a3
        tst.l   d0                      ; OK
        beq.s   uex_exit                ; yes, done then
*
uex_exrj
        move.l  stk_jid(sp),d1          ; remove our new job
        move.l  d0,-(sp)
        move.l  d0,d3
        moveq   #sms.frjb,d0
        trap    #do.sms2
        move.l  (sp)+,d0                ; and return original error
        bra.s   uex_exit
*
uex_excl
        bsr.s   uex_clos                ; close the file
uex_exit
        add.w   #uex.fram,sp
        movem.l (sp)+,uexreg
        rts
uex_clos
        move.l  stk_chid+4(sp),a0       ; the file
        move.l  stk_name+4(sp),a2       ; the name
        moveq   #sms.myjb,d1            ; my copy
        move.l  stk_ucod+4(sp),a1       ; using the user code
        jmp     8(a1)                   ; close it
*
        end
