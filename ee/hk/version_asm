; HOTKEY Versions

	xdef	hk_vers

;	2.00	first release version of HOTKEY II.
;
;	2.01	TK2_EXT fixed (crashed in version 2.00).
;
;	2.02	GRABBER defaults to 32k.
;		Duplicate and short names allowed for HOT_RES etc.
;		N.B. some 2.02 were sent out as 2.01!!!
;
;	2.03	Revision 1
;		128 max HOTKEYS.
;		Upper and lower case optionally distinguished.
;		TH_ENTRY, TH_EXEC vectors linked into end of THING list.
;		Item and Thing heap entries allocated to owner 0.
;
;	2.04	HK_GUARD vectored.
;		Psion guardian windows incresed to full screen.
;
;	2.05	Occasional 'IN USE' problem with HOT_RES and HOT_CHP fixed.
;		Version 0.02 of THING.
;
;	2.06	Supervisor stack crawl (in last line recall) fixed.
;
;	2.07	TH_REMOV vectored routine is now allowed to smash A2, just
;		  as it says in the document (Version 0.03 of THING).
;
;	2.08	HOT_WAKE added.
;
;	2.09	HOT_RES1, HOT_CHP1 and HOT_LOAD1 versions of HOT_RES, HOT_CHP
;		  and HOT_LOAD try to WAKE a Job of the appropriate name
;		  before trying to execute it. HOT_WAKE will try to execute
;		  a THING of the same name if the wake fails.
;
;	2.10	Reorganised
;
;	2.11	Get previous string from stuffer buffer fixed
;		Registers preserved on Set Stuffer Buffer vector
;
;	2.12	= 2.11 !!
;
;	2.13	Set stuffer buffer will not replicate top string
;
;	2.14	HOT_LOAD fixed for Minerva
;
;	2.15	HOTKEY job no longer removed if a HOT_LOAD fails during
;		loading.
;		Case of HOT_RES / HOT_CHP Hotkeys preserved.
;		Removing HOT_RES or HOT_THING Hotkeys does not remove
;		the Thing itself. Removing HOT_CHP Hotkeys still does.
;
;	2.16	New Thing find name does not lowercase the name.
;
;	2.17	EXEP tries thing first if no extra parameters.
;
;	2.18	Back to 128 Hotkeys (when did it revert to 64?)
;		Freeze option added
;
;	2.19	Re-initialisation ignored, but writes message
;
;	2.20	Internal re-organisation
;
;	2.21	Parameter passing to programs implemented.
;		Wake name allowed to be different from Thing / File name
;
;	2.22	 in parameter stuffs current buffer
;
;	2.23	EXEP corrected for fault in V2.21/2.22
;
;	2.24	Impure Option restored (missing in 2.21-2.23)
;		Job names allowed on HOT_WAKE and HOT_THING.
;
;	2.25	Wake works even if there are no suitable windows.
;
;	2.26	HOT_REMV an upper case letter does not remove lower case.
;
;	2.27	Guardian code modified to reduce stack usage on job startup.
;
;	2.28	HOT_KEY, HOT_CMD etc. use name of item if pointer is not set.
;
;	2.29	More generous allowances for string parameters
;
;	2.30	New keywords Fex etc... (PW & MK)
;		SMSQ/E version now configurable again (MK)
;
;	2.31	New keyword HOT_GETSTUFF$ (MK)

hk_vers equ    '2.31'

	end
