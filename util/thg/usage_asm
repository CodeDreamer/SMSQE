* Usage list maintenance                v0.00   Feb 1988  J.R.Oakley  QJUMP
*
        section thing
*
        include 'dev8_mac_assert'
        include 'dev8_keys_chn'
        include 'dev8_keys_chp'
        include 'dev8_keys_iod'
        include 'dev8_keys_jcb'
        include 'dev8_keys_err'
        include 'dev8_keys_qlv'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
*
        xref    th_hrzap
*
        xdef    th_addu
        xdef    th_remu
        xdef    th_efre
        xdef    th_ulku
        xdef    th_newth
        xdef    th_remth
        xdef    th_alchp
        xdef    th_rechp
        xdef    th_frmth
        xdef    th_nxtu
        page
*+++
* Mark a thing as used by the job given.  This QDOS version does it by
* allocating a small chunk of heap, which is linked into a list pointed
* to by the usage header: the driver for this heap entry unlinks it from
* the list, ensuring that the thing is freed if the Job is removed.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, ITNF, IJOB
*       D1      Job ID                          Job ID
*       D2/D3                                   smashed
*       A0                                      smashed
*       A1      pointer to linkage block        preserved
*       A2                                      result
*       A3                                      smashed
*       A6      pointer to system variables     preserved
*---
th_addu
        tst.b   th_nshar(a1)            ; is the Thing shareable?
        bpl.s   tha_calu                ; yes, always OK
        moveq   #err.fdiu,d0            ; no, could be in use
        tst.l   th_usage(a1)            ; is it?
        bne.s   tha_exit                ; yes, rats
tha_calu
        move.l  th_use(a1),d0           ; do we have a USE routine?
        bsr.s   call                    ; we don't care
        bra.s   tha_link                ; if we did then link in block
*
        moveq   #thu.ulnk,d0            ; allocate space for usage block
        bsr.s   th_alchp                ; allocate it
tha_link
        bne.s   tha_exit                          ; ...oops
        move.l  d1,chp_ownr(a0)                   ; fill in owner
        lea     th_frfre+iod_iolk-iod_clos(a1),a3 ; point to "driver"
        move.l  a3,chp_drlk(a0)                   ; fill it in in usage block
        addq.l  #th_usage,a1                      ; link to usage list
        add.w   #thu_link,a0                      ; link is here
        move.w  mem.llst,a3                       ; link new block after it
        jsr     (a3)
        subq.l  #th_usage,a1
tha_exit
        tst.l   d0
        rts
        page
*
* This routine calls the routine pointed to by D0, or returns one word
* further on than expected...
*
call
        beq.s   no_call
callreg reg     d1/d4-d7/a1/a3-a6
        movem.l callreg,-(sp)
        bsr.s   calld0
        movem.l (sp)+,callreg
        rts
calld0
        move.l  d0,-(sp)
        rts
no_call
        addq.l  #2,(sp)
        rts
*+++
* Allocate space in the common heap: note that the returned A0
* points to the heap header, not the "first usable" memory, and that
* D0 should include the memory required for the header ($10 bytes).
*
*       Registers:
*               Entry                           Exit
*       D0      amount of heap required         error code
*       A0                                      heap header
*---
th_alchp
achpreg reg     d1-d3/a1-a3
        movem.l achpreg,-(sp)
        move.l  d0,d1                   ; amount of heap to get
        move.w  mem.achp,a2             ; get it from common heap
        jsr     (a2)
        tst.l   d0
        movem.l (sp)+,achpreg
        rts
*+++
* Return memory to the common heap: A0 should point to the heap header,
* not the "usable memory".
*
*       Registers:
*               Entry                           Exit
*       A0      heap entry                      preserved
*---
th_rechp
rchpreg reg     d0-d3/a0-a3
        movem.l rchpreg,-(sp)
        move.w  mem.rchp,a2
        jsr     (a2)
        movem.l (sp)+,rchpreg
        rts
        page
*+++
* Remove a usage block for a given job from the list.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, ITNF
*       D1      Job ID                          preserved
*       D2      parameter                       result
*       D3      parameter                       preserved
*       A1      thing linkage block             smashed
*       A2      parameter                       result
*       A3                                      smashed
*---
th_remu
        lea     th_usage(a1),a0         ; no code, point to first link
thr_loop
        move.l  (a0),d0                  ; next usage block
        beq.s   thr_exnf                 ; no more, not found
        move.l  d0,a0                    ; point to it
        cmp.l   chp_ownr-thu_link(a0),d1 ; owned by the given Job?
        bne.s   thr_loop                 ; no
*
        cmp.l   d1,d6                   ; is owner us?
        beq.s   thr_free                ; yes, just free usage entry
        move.l  (sp)+,a4                ; this should be safe
        moveq   #err.nc,d3              ; no, owner job didn't complete...
        exg     a5,a6                   ; in case we're SuperBASIC
        moveq   #sms.frjb,d0            ; ...'cos we removed it!
        trap    #do.sms2                ; $$$$ better have an empty stack !!!!
        exg     a5,a6
        moveq   #0,d0                   ; no problems
        jmp     (a4)                    ; return
thr_free
        sub.w   #thu_link,a0            ; point to heap entry
        move.l  th_free(a1),d0          ; call free code if present
        bsr.s   call
        bra.s   th_ulku                 ; and unlink
thr_rchp
        bsr.s   th_rechp                ; no code, return heap entry
        page
*+++
* This routine unlinks a usage block, which will already have been
* returned to the heap. It is entered either from TH_REMU as a result of
* an explicit call to stop using a given thing, or via the operating
* system as a result of the owner Job's removal.
*
*       Registers:
*               Entry                           Exit
*       A0      usage block to remove
*       A1      thing linkage                   preserved
*       A3                                      smashed
*---
th_ulku
        addq.l  #th_usage,a1            ; point to usage list head
        add.w   #thu_link,a0            ; and link pointer
        move.w  mem.rlst,a3             ; and remove this entry from the list
        jsr     (a3)
        subq.w  #th_usage,a1            ; point to thing linkage
        rts
*
thr_exnf
        moveq   #err.itnf,d0
        rts
*+++
* External call to free thing: this is what is called when a thing is freed
* by its owner Job being removed.  In this case we can't pass parameters
* to the thing telling it which instance to remove, so we need a different
* entry point.
*
*       Registers:
*               Entry                           Exit
*       A0      usage block to remove
*       A1                                      thing linkage block
*       A2                                      smashed
*       A3                                      smashed
*       A6      system variables
*---
th_efre
        move.l  chp_drlk(a0),a1                   ; point to driver...
        lea     iod_clos-iod_iolk-th_frfre(a1),a1 ; ...and thus linkage block
        move.l  th_ffree(a1),d0                   ; this is a forced free
        bsr     call                              ; call code
        bra.s   th_ulku                           ; and unlink
        bra     thr_rchp                          ; no code, just return heap
        page
*+++
* Remove a Thing: this is only possible if there are no
* Jobs using the thing at the time.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or FDIU
*       A0                                      smashed
*       A1      thing linkage block             preserved
*---
th_remth
        move.l  a2,-(sp)                ; save A2
        tst.l   th_usage(a1)            ; anyone using this thing?
        bne.s   the_exiu                ; yes, give up
        move.l  th_remov(a1),d0         ; no, tidy up
        bsr     call
        bra.s   the_exit                ; somebody else knows how
        bsr.s   thz_rlnk                ; we know how, return linkage
        tst.l   d0
        bra.s   the_exit
the_exiu
        moveq   #err.fdiu,d0
the_exit
        move.l  (sp)+,a2
        rts
*+++
* Initialise a new Thing. This initialises the "close" routine
* address, used when a Thing-owning Job is removed, and the linkage block's
* own driver address, used if the Thing is owned by a Job other than the system.
*
*       Registers:
*               Entry                           Exit
*       A0                                      smashed
*       A1      thing linkage block             preserved
*---
th_newth
        lea     th_efre(pc),a0          ; point to "close" routine
        move.l  a0,th_frfre(a1)         ; fill it in
        lea     th_hrzap(pc),a0         ; and "zap" routine
        move.l  a0,th_frzap(a1)         ; fill that in
        lea     th_frzap+iod_iolk-iod_clos(a1),a0
        move.l  a0,chp_drlk-chp_end(a1) ; driver is REMOVe code
        clr.l   th_usage(a1)            ; no usage list at the moment
        rts
        page
*+++
* Force remove a thing: to do this we must remove all Jobs using the
* Thing.  If the current Job is using the thing then it shouldn't
* be removed directly.
*
*       Registers:
*               Entry                           Exit
*       D6      current Job ID                  preserved
*       A1      thing linkage block             smashed  
*       A2/A3                                   smashed
*---
th_frmth
        move.l  th_usage(a1),-(sp)      ; keep usage list
        move.l  th_remov(a1),d0
        bsr     call                    ; call remove code...
        bra.s   thz_kilj                ; ...and kill jobs if there was some
        bsr.s   thz_rlnk                ; otherwise just return linkage
thz_kilj
        move.l  (sp)+,d0                ; now remove all entries in usage list
thz_rmul
        beq.s   thz_exit                ; no more users
        move.l  d0,a2                   ; usage block is
        lea     -thu_link(a2),a0        ; here
        move.l  chp_ownr(a0),d1         ; keep its owner
        bsr     th_efre                 ; and release as if Job got removed
        tst.l   d1                      ; is owner system?
        beq.s   thz_nxtu                ; yes, I hope it can cope!
        cmp.l   d1,d6                   ; is owner current Job?
        beq.s   thz_nxtu                ; yes, it ought to know the Thing's gone
        moveq   #0,d2
        moveq   #sms.injb,d0            ; wot abaht this owner then?
        trap    #do.sms2
        lea     thz_kill(pc),a3         ; kill self routine
        move.l  a3,jcb_pc-jcb_end(a0)   ; is what it'll do when scheduled!
        move.b  #127,$13-jcb_end(a0)    ; and at high priority, too
        clr.w   jcb_wait-jcb_end(a0)    ; and it ain't suspended now
thz_nxtu
        move.l  (a2),d0                 ; next usage block
        bra.s   thz_rmul
thz_exit
        rts                             ; back to zap code
thz_rlnk
        lea     -chp_end-th_nxtth(a1),a0 ; point to heap entry
        bra     th_rechp                 ; and return it
thz_kill
        moveq   #sms.myjb,d1            ; I
        moveq   #err.nc,d3              ; didn't complete
        moveq   #sms.frjb,d0            ; 'cos I died myself
        trap    #do.sms2
        page
*+++
* Find the next Job using the given Thing: if the usage block passed
* no longer exists then the error IJOB is returned.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or IJOB
*       D2                                      owner of passed usage block
*       A1      thing linkage block             next usage block
*       A2      usage block or 0
*---
th_nxtu
        move.l  a2,d0                   ; is there a current usage block?
        bne.s   thn_fjob                ; yes
        move.l  th_usage(a1),a1         ; no, point to the first one
        bra.s   thn_exit                ; and that's it
thn_fjob
        lea     th_usage(a1),a1
thn_loop
        move.l  (a1),d2                 ; next usage block
        beq.s   thn_exij                ; none, bother
        move.l  d2,a1
        cmp.l   d2,d0                   ; is this the current one?
        bne.s   thn_loop                ; no, carry on
*
        move.l  chn_ownr-thu_link(a2),d2 ; get owner
        move.l  (a1),a1                  ; and next block pointer
        moveq   #0,d0
thn_exit
        rts
thn_exij
        moveq   #err.ijob,d0
        bra.s   thn_exit
*
        end
